import re
from datetime import datetime

def clean_event_data(events):
    """Clean and standardize event data"""
    cleaned_events = []
    
    for event in events:
        try:
            # Create a clean copy
            clean_event = event.copy()
            
            # Clean title - remove excessive whitespace and special chars
            if 'title' in clean_event:
                clean_event['title'] = clean_title(clean_event['title'])
            
            # Clean date format
            if 'date' in clean_event:
                date = clean_date(clean_event['date'])
                clean_event['start_hour'] = extract_start_hour(clean_event['date'])
                clean_event['end_hour'] = extract_end_hour(clean_event['date'])
                clean_event['date'] = date
            
            # Clean price/cost
            if 'cost' in clean_event:
                clean_event['cost'] = clean_price(clean_event['cost'])
            
            # Clean venue/location
            if 'venue' in clean_event:
                clean_event['venue'] = clean_venue(clean_event['venue'])
            
        
            cleaned_events.append(clean_event)
        except Exception as e:
            print(f"Error cleaning event data: {e}")
    
    return cleaned_events

def clean_title(title):
    """Clean event title"""
    if not title:
        return ""
    
    # Remove excessive whitespace
    title = re.sub(r'\s+', ' ', title).strip()
    
    # Remove special characters that might cause issues
    title = re.sub(r'[^\w\s\-\.,;:&\'"\(\)]+', '', title)
    
    return title

def extract_start_hour(date_str):
    """
    Extract and format the start hour in 24-hour format (HH:MM)
    from various date string formats
    """
    import re
    from datetime import datetime
    
    if not date_str:
        return ""
    
    # Various patterns to match time information
    patterns = [
        # Pattern: "at 7:00 pm" or "at 07:00 pm"
        r'at\s+(\d{1,2}:\d{2})\s*(am|pm|AM|PM)',
        
        # Pattern: "7:00 pm" or "07:00 pm" (without "at")
        r'(\d{1,2}:\d{2})\s*(am|pm|AM|PM)',
        
        # Pattern: "7 pm" or "07 pm" (without minutes)
        r'(\d{1,2})\s*(am|pm|AM|PM)',
        
        # Pattern: "19:00" (24-hour format)
        r'(\d{1,2}:\d{2})(?!\s*(am|pm|AM|PM))',
        
        # Pattern: "7.00 pm" or "07.00 pm" (with period instead of colon)
        r'(\d{1,2})\.(\d{2})\s*(am|pm|AM|PM)'
    ]
    
    # Try each pattern
    for pattern in patterns:
        time_match = re.search(pattern, date_str)
        if time_match:
            try:
                # For patterns with period instead of colon
                if '.' in pattern:
                    hour = time_match.group(1)
                    minute = time_match.group(2)
                    am_pm = time_match.group(3).lower() if len(time_match.groups()) >= 3 else None
                    
                    if am_pm:
                        time_str = f"{hour}:{minute} {am_pm}"
                        time_format = "%I:%M %p"
                    else:
                        time_str = f"{hour}:{minute}"
                        time_format = "%H:%M"
                        
                # For patterns without minutes (e.g., "7 pm")
                elif ':' not in pattern and not '.' in pattern:
                    hour = time_match.group(1)
                    am_pm = time_match.group(2).lower()
                    time_str = f"{hour}:00 {am_pm}"
                    time_format = "%I:%M %p"
                    
                # For 24-hour format without AM/PM
                elif len(time_match.groups()) < 2 or time_match.group(2) is None:
                    time_str = time_match.group(1)
                    time_format = "%H:%M"
                    
                # Standard format with colon and AM/PM
                else:
                    time_str = time_match.group(1)
                    am_pm = time_match.group(2).lower()
                    time_str = f"{time_str} {am_pm}"
                    time_format = "%I:%M %p"
                
                # Parse the time string
                time_obj = datetime.strptime(time_str, time_format)
                
                # Return in 24-hour format
                return time_obj.strftime("%H:%M")
                
            except (ValueError, IndexError) as e:
                # If parsing fails, try the next pattern
                continue
    
    # If we reach here, no time information was found
    return ""

def extract_end_hour(date_str):
    """
    Extract and format the end hour in 24-hour format (HH:MM)
    from various date string formats
    """
    import re
    from datetime import datetime
    
    if not date_str:
        return ""
    
    # First, look for pattern with explicit "to" indicating end time
    to_patterns = [
        # Pattern: "to 9:00 pm" or "to 09:00 pm"
        r'to\s+(\d{1,2}:\d{2})\s*(am|pm|AM|PM)',
        
        # Pattern: "to 9 pm" or "to 09 pm" (without minutes)
        r'to\s+(\d{1,2})\s*(am|pm|AM|PM)',
        
        # Pattern: "to 21:00" (24-hour format)
        r'to\s+(\d{1,2}:\d{2})(?!\s*(am|pm|AM|PM))',
        
        # Pattern: "to 9.00 pm" or "to 09.00 pm" (with period instead of colon)
        r'to\s+(\d{1,2})\.(\d{2})\s*(am|pm|AM|PM)'
    ]
    
    # Try each "to" pattern first
    for pattern in to_patterns:
        time_match = re.search(pattern, date_str)
        if time_match:
            try:
                # For patterns with period instead of colon
                if '.' in pattern:
                    hour = time_match.group(1)
                    minute = time_match.group(2)
                    am_pm = time_match.group(3).lower() if len(time_match.groups()) >= 3 else None
                    
                    if am_pm:
                        time_str = f"{hour}:{minute} {am_pm}"
                        time_format = "%I:%M %p"
                    else:
                        time_str = f"{hour}:{minute}"
                        time_format = "%H:%M"
                        
                # For patterns without minutes (e.g., "to 9 pm")
                elif ':' not in pattern and not '.' in pattern:
                    hour = time_match.group(1)
                    am_pm = time_match.group(2).lower()
                    time_str = f"{hour}:00 {am_pm}"
                    time_format = "%I:%M %p"
                    
                # For 24-hour format without AM/PM
                elif len(time_match.groups()) < 2 or time_match.group(2) is None:
                    time_str = time_match.group(1)
                    time_format = "%H:%M"
                    
                # Standard format with colon and AM/PM
                else:
                    time_str = time_match.group(1)
                    am_pm = time_match.group(2).lower()
                    time_str = f"{time_str} {am_pm}"
                    time_format = "%I:%M %p"
                
                # Parse the time string
                time_obj = datetime.strptime(time_str, time_format)
                
                # Return in 24-hour format
                return time_obj.strftime("%H:%M")
                
            except (ValueError, IndexError) as e:
                # If parsing fails, try the next pattern
                continue
    
    # If no "to" pattern was found, check if there are two times mentioned
    # and use the second one as the end time
    try:
        # Find all time patterns
        all_times = re.findall(r'(\d{1,2}:\d{2})\s*(am|pm|AM|PM)', date_str)
        
        # If we found at least two times, use the second one as end time
        if len(all_times) >= 2:
            time_str = all_times[1][0]
            am_pm = all_times[1][1].lower()
            
            # Parse the time string
            time_obj = datetime.strptime(f"{time_str} {am_pm}", "%I:%M %p")
            
            # Return in 24-hour format
            return time_obj.strftime("%H:%M")
            
    except (ValueError, IndexError):
        pass
    
    # If we reach here, no end time information was found
    return ""
def clean_date(date_str):
    """Clean and standardize date format to YYYY-MM-DD"""
    import re
    from datetime import datetime
    
    if not date_str:
        return ""
    
    # Handle specific format: "Sat  17 May  2025 at 03:00 pm to 05:00 pm (IST)"
    # First, try to extract just the date part using regex
    # This regex accounts for possible multiple spaces between components
    date_match = re.search(r'(\w+)\s+(\d{1,2})\s+(\w+)\s+(\d{4})', date_str)
    
    if date_match:
        try:
            # Extract individual components
            weekday = date_match.group(1).strip()
            day = date_match.group(2).strip()
            month = date_match.group(3).strip()
            year = date_match.group(4).strip()
            
            # Create a standardized date string
            standardized_date = f"{day} {month} {year}"
            
            # Parse with appropriate format
            try:
                # Try full month name format
                parsed_date = datetime.strptime(standardized_date, "%d %B %Y")
            except ValueError:
                # Try abbreviated month name format
                parsed_date = datetime.strptime(standardized_date, "%d %b %Y")
                
            # Format to YYYY-MM-DD
            return parsed_date.strftime("%Y-%m-%d")
        except (ValueError, IndexError):
            # If the specific pattern match fails, continue to fallback methods
            pass
    
    # Try to match simpler patterns where date components may be separated differently
    basic_date_match = re.search(r'(\d{1,2})[\/\s\-]+(\w+|\d{1,2})[\/\s\-]+(\d{4}|\d{2})', date_str)
    if basic_date_match:
        try:
            day_or_month1 = basic_date_match.group(1).strip()
            month_or_day = basic_date_match.group(2).strip()
            year = basic_date_match.group(3).strip()
            
            # Handle 2-digit year
            if len(year) == 2:
                year = f"20{year}" if int(year) < 50 else f"19{year}"
            
            # Try various date formats
            date_formats = [
                f"{day_or_month1} {month_or_day} {year}",  # Day Month Year
                f"{month_or_day} {day_or_month1} {year}"   # Month Day Year
            ]
            
            for date_format in date_formats:
                for fmt in ["%d %B %Y", "%d %b %Y", "%m %d %Y", "%d %m %Y"]:
                    try:
                        parsed_date = datetime.strptime(date_format, fmt)
                        return parsed_date.strftime("%Y-%m-%d")
                    except ValueError:
                        continue
        except (ValueError, IndexError):
            pass
    
    # Fallback to more general approach
    try:
        # Normalize date string
        normalized_date_str = date_str.replace(',', ' ').strip()
        
        # Extract date part (before "at" or "|" if present)
        if " at " in normalized_date_str:
            normalized_date_str = normalized_date_str.split(" at ")[0].strip()
        
        date_parts = re.split(r'[|]', normalized_date_str)
        normalized_date_str = date_parts[0].strip()
        
        # Try to add year if missing
        if not re.search(r'\d{4}', normalized_date_str):
            current_year = datetime.now().year
            normalized_date_str += f" {current_year}"
        
        # Remove extra spaces
        normalized_date_str = re.sub(r'\s+', ' ', normalized_date_str)
        
        # Try to parse with multiple formats
        formats_to_try = [
            "%a %d %b %Y", 
            "%a %d %B %Y", 
            "%d %b %Y", 
            "%d %B %Y", 
            "%b %d %Y", 
            "%B %d %Y", 
            "%m/%d/%Y", 
            "%d/%m/%Y",
            "%Y-%m-%d"
        ]
        
        for fmt in formats_to_try:
            try:
                parsed_date = datetime.strptime(normalized_date_str, fmt)
                return parsed_date.strftime("%Y-%m-%d")
            except ValueError:
                continue
                
        # If all else fails, return the normalized string
        return normalized_date_str
    except Exception as e:
        # If any unexpected error occurs, return the original string
        print(f"Error parsing date '{date_str}': {e}")
        return date_str

def clean_price(price):
    """Clean price/cost information to ensure database compatibility"""
    if not price:
        return 0.0
    
    if price == "Free" or (isinstance(price, str) and price.lower() == "free"):
        return 0.0
    
    try:
        # For numeric values, just return them
        if isinstance(price, (int, float)):
            return float(price)
            
        # Extract numeric value
        import re
        price_match = re.search(r'(\d+(?:\.\d+)?)', str(price))
        if price_match:
            return float(price_match.group(1))
    except:
        pass
    
    return 0.0
def clean_venue(venue):
    """Clean venue/location information"""
    if not venue:
        return ""
    
    # Remove excessive whitespace
    venue = re.sub(r'\s+', ' ', venue).strip()
    
    return venue

    # Look for time information after a separator like |
    if '|' in date_str:
        parts = date_str.split('|')
        if len(parts) > 1:
            time_part = parts[1].strip()
            return time_part
    
    return ""